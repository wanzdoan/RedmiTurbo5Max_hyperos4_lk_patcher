"""Differential native AVB and image selection, with fault controls."""
import json
from slot_replay import *

H4=ROOT/'experiments/lk_h4_eng_policy_recovery_fix_v2/lk_H4_ENG_POLICY_AUTO_HWC_RECOVERY_V2_MTE_OFF_TEST.img'
ENG=ROOT/'experiments/lk_h3_eng_locked_rom_policy_v1/hwc_auto_region_v1/lk_hyperos3_ENG_STOCK_AUTO_HWC_CN_GLOBAL_EEA_TW_TEST.img'

def main():
    cases=[]
    for kind,path in [('h4',H4),('eng',ENG)]:
        image=path.read_bytes()
        expected='48912f04727fede359a77dbe57dcb636cbb70f52121f7052ab00af46fbc22c53' if kind=='h4' else 'fe60552c60d3495ad479e34e2610ab37acca064c367f8fce750989f13f4e12a0'
        assert sha(image)==expected
        replay=Replay(image,kind);result=replay.run()
        assert result['returned'] and result['error'] is None and result['result']==0
        result['loader']=replay.load();assert result['loader']['reached_copy_sink'] and result['loader']['error'] is None
        result['root_handoff']=replay.handoff()
        cases.append(dict(kind=kind,scenario='clean_locked',result=result))
    a,b=cases
    assert a['result']['cmdline']==b['result']['cmdline']
    assert a['result']['loader']['cmdlines']==b['result']['loader']['cmdlines']
    assert a['result']['loader']['selected_images']==b['result']['loader']['selected_images']
    assert a['result']['root_handoff']['packets']==b['result']['root_handoff']['packets']
    controls=[]
    for fault,rollback,wanted in [('init_boot_hash',0,3),('vbmeta_signature',0,3),('init_boot_io',0,2),(None,64,4)]:
        replay=Replay(H4.read_bytes(),'h4',fault=fault,rollback_value=rollback)
        result=replay.run()
        assert result['returned'] and result['error'] is None and result['result']==wanted,(fault,result)
        controls.append(dict(fault=fault,rollback_fixture=rollback,expected_result=wanted,result=result))
    inventory=[]
    for name in ['MT6991_Android_scatter.txt','vbmeta.img','vbmeta_system.img','vbmeta_vendor.img','boot.img','init_boot.img','vendor_boot.img','dtbo.img']:
        path=ROM/name;data=path.read_bytes();inventory.append(dict(file=name,bytes=len(data),sha256=sha(data)))
    report=dict(status='PASS_NATIVE_AVB_DIFFERENTIAL_NOT_PHYSICAL_BOOT',rom=str(ROM.parent),inputs=inventory,
        clean_cases=cases,negative_controls=controls,
        observations=['Both exact firmware mains validate the same signed China ROM with locked4 fixture and flags0',
            'Native RSA/descriptor parsing/hash comparison/filter run; SHA engine and bulk IO/copy are host backends',
            'Both loaders select identical boot/vendor_boot/init_boot buffers and produce identical AVB command lines',
            'Five root handoff packets match under certified-key and boot-state fixtures',
            'Signature corruption, init_boot corruption, IO error and rollback64 produce correct failure results',
            'No tested clean-ROM divergence found up to the image-buffer handoff boundary'],
        limits=['Not a full LK boot: mode selection, RAM placement, DTB/bootconfig assembly and kernel not executed',
                'No live image readback, boot mode, BCB, pstore or recovery log',
                'Rollback0, stored lock4, hardware CN, GUID, absent persistence and zero-filled partition tails are fixtures',
                'Root OEM-key extraction and secure-monitor reply are fixtures, real trusted-world validation unmeasured',
                'This test invokes main directly and cannot distinguish BL2/preloader handoff compatibility',
                'No filesystem/kernel dm-verity mount/FBE/banking/Integrity validation',
                'Firmware files unchanged; no device access'])
    (HERE/'clean_rom_comparison.json').write_text(json.dumps(report,indent=2)+'\n')
    print(report['status'],'2clean native slot/loader/handoff comparisons;4fault controls')

if __name__=='__main__':main()
