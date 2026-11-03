
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff000090722ae8(void)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  undefined4 *puVar4;
  undefined4 *puVar5;
  undefined4 *puVar6;
  undefined4 *local_28;
  
  lVar3 = FUN_ffff0000907198dc
                    (0x10000,0x10000,0xffffffffffffffff,0,0,s_DPM_reserved_ffff0000907f4541);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s__DPM__mblock_reserve_fail_ffff0000907e2917);
    return 0xfffffffb;
  }
  local_28 = (undefined4 *)0x0;
  uVar1 = FUN_ffff00009077ba64
                    (PTR_DAT_ffff000090839928,s_vm_DPM_reserved_ffff0000907fedf7,0x10000,&local_28,
                     0xc,lVar3,0,0);
  if (uVar1 != 0) {
    FUN_ffff00009077e11c(s__DPM__vmm_alloc_physical_failed_ffff0000907d421b);
    goto LAB_ffff000090722d08;
  }
  FUN_ffff000090722adc();
  puVar4 = local_28;
  uVar1 = FUN_ffff000090785d18(&DAT_ffff0000907d423c,s_dpmpm_ffff0000907e4538,local_28,0xc000);
  if ((int)uVar1 < 0) {
    FUN_ffff00009077e11c(s__DPM__load_DPM_image_fail__s_ffff0000907f97b4,s_dpmpm_ffff0000907e4538);
  }
  else {
    puVar6 = local_28 + 0x3000;
    uVar2 = FUN_ffff000090785d18(&DAT_ffff0000907d423c,s_dpmdm_ffff0000907eaa90,puVar6,0x4000);
    if ((int)uVar2 < 0) {
      FUN_ffff00009077e11c(s__DPM__load_DPM_image_fail__s_ffff0000907f97b4,s_dpmdm_ffff0000907eaa90)
      ;
      uVar1 = uVar2;
    }
    else {
      if (0xc000 < uVar1) {
        _DAT_ffff0000100010b4 = _DAT_ffff0000100010b4 | 0x2ffff;
        return 0xffffffff;
      }
      if (uVar1 < 0x8001) {
        if (0x4000 < uVar1) {
          _DAT_ffff000010940000 = _DAT_ffff000010940000 | 0x10000000;
          goto LAB_ffff000090722c7c;
        }
        _DAT_ffff000010940000 = _DAT_ffff000010940000 | 0x20000000;
        if (uVar1 != 0) goto LAB_ffff000090722c7c;
      }
      else {
LAB_ffff000090722c7c:
        uVar1 = uVar1 + 4;
        puVar5 = (undefined4 *)&DAT_ffff000010900000;
        do {
          uVar1 = uVar1 - 4;
          *puVar5 = *puVar4;
          puVar5 = puVar5 + 1;
          puVar4 = puVar4 + 1;
        } while (4 < uVar1);
      }
      if (0 < (int)uVar2) {
        uVar2 = uVar2 + 4;
        puVar4 = (undefined4 *)&DAT_ffff000010920000;
        do {
          uVar2 = uVar2 - 4;
          *puVar4 = *puVar6;
          puVar4 = puVar4 + 1;
          puVar6 = puVar6 + 1;
        } while (4 < uVar2);
      }
      _DAT_ffff000010946004 = 0;
      _DAT_ffff000010946008 = 0;
      DataSynchronizationBarrier(3,3,0);
      _DAT_ffff000010947074 = _DAT_ffff000010947074 | 1;
      uVar1 = 0;
    }
  }
  FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_28);
LAB_ffff000090722d08:
  FUN_ffff00009071a7f0(lVar3);
  return uVar1;
}


