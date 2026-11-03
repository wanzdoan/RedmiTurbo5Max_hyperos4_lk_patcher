// ffff00009078d2ac  size=688  FUN_ffff00009078d2ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078d2ac(void)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  long *plVar4;
  long lVar5;
  byte *pbVar6;
  char *pcVar7;
  uint uVar8;
  byte *pbVar9;
  
  uVar2 = _DAT_ffff000016701714 & _DAT_ffff000016701710;
  uVar8 = _DAT_ffff000016701704 & _DAT_ffff000016701700;
  _DAT_ffff000016701700 = uVar8;
  if (uVar8 != 0) {
    if ((uVar8 >> 0x11 & 1) != 0) {
      plVar4 = (long *)FUN_ffff00009078b04c(1,0);
      if (plVar4 == (long *)0x0) {
        pcVar7 = s__USB__QMU__no_udc_endpoint_is_fo_ffff0000907f1ee9;
LAB_ffff00009078d344:
        FUN_ffff00009077de54(pcVar7);
      }
      else {
        pbVar6 = (byte *)plVar4[5];
        uVar1 = ((ulong)_DAT_ffff000016701618 - plVar4[1]) +
                ((ulong)(_DAT_ffff0000167014c4 >> 8 & 0xf) << 0x20);
        if ((uVar1 & 0xfffffffc0) == 0) {
          pbVar9 = (byte *)(plVar4[2] + (uVar1 & 0xffffffff0));
        }
        else {
          pbVar9 = (byte *)0x0;
        }
        FUN_ffff000090707ce4(pbVar6,0x10);
        if (pbVar6 != pbVar9) {
          pcVar7 = s__USB__QMU__no_correct_RX_req_is_f_ffff0000907d520a;
          do {
            if ((*pbVar6 & 1) != 0) break;
            FUN_ffff000090707ce4(pbVar6,0x10);
            lVar5 = *plVar4;
            if ((lVar5 == 0) || (*(byte **)(lVar5 + 0x28) != pbVar6)) goto LAB_ffff00009078d344;
            *(uint *)(lVar5 + 0x30) = (uint)*(ushort *)(pbVar6 + 0xc);
            FUN_ffff00009078b0cc(plVar4,0);
            if ((ulong)plVar4[5] < (ulong)plVar4[3]) {
              pbVar6 = (byte *)(plVar4[5] + 0x10);
            }
            else {
              pbVar6 = (byte *)plVar4[2];
            }
            plVar4[5] = (long)pbVar6;
          } while (pbVar6 != pbVar9);
        }
      }
    }
    if ((uVar8 >> 1 & 1) != 0) {
      plVar4 = (long *)FUN_ffff00009078b04c(1,0x80);
      if (plVar4 == (long *)0x0) {
        pcVar7 = s__USB__QMU__no_udc_endpoint_is_fo_ffff0000907d5231;
LAB_ffff00009078d434:
        FUN_ffff00009077de54(pcVar7);
      }
      else {
        pbVar6 = (byte *)plVar4[5];
        uVar1 = ((ulong)_DAT_ffff000016701518 - plVar4[1]) +
                ((ulong)(_DAT_ffff000016701484 >> 8 & 0xf) << 0x20);
        if ((uVar1 & 0xfffffffc0) == 0) {
          pbVar9 = (byte *)(plVar4[2] + (uVar1 & 0xffffffff0));
        }
        else {
          pbVar9 = (byte *)0x0;
        }
        FUN_ffff000090707ce4(pbVar6,0x10);
        if (pbVar6 != pbVar9) {
          pcVar7 = s__USB__QMU__no_correct_TX_req_is_f_ffff000090801703;
          do {
            if ((*pbVar6 & 1) != 0) break;
            FUN_ffff000090707ce4(pbVar6,0x10);
            lVar5 = *plVar4;
            if ((lVar5 == 0) || (*(byte **)(lVar5 + 0x28) != pbVar6)) goto LAB_ffff00009078d434;
            *(uint *)(lVar5 + 0x30) = (uint)*(ushort *)(pbVar6 + 0xc);
            FUN_ffff00009078b0cc(plVar4,0);
            if ((ulong)plVar4[5] < (ulong)plVar4[3]) {
              pbVar6 = (byte *)(plVar4[5] + 0x10);
            }
            else {
              pbVar6 = (byte *)plVar4[2];
            }
            plVar4[5] = (long)pbVar6;
          } while (pbVar6 != pbVar9);
        }
      }
    }
  }
  uVar8 = _DAT_ffff0000167017c0;
  _DAT_ffff0000167017c0 = uVar8;
  if (uVar2 != 0) {
    if ((uVar2 & 0x60000) != 0) {
      if ((_DAT_ffff0000167017c0 >> 1 & 1) != 0) {
        FUN_ffff00009077de54(s__USB__QMU__Rx_EP_d_CS_error__ffff0000907e54ff,1);
      }
      if ((uVar8 >> 0x11 & 1) != 0) {
        FUN_ffff00009077de54(s__USB__QMU__RX_EP_d_Length_error_ffff0000907e6caf,1);
      }
    }
    uVar3 = _DAT_ffff000016701780;
    _DAT_ffff000016701780 = uVar3;
    _DAT_ffff0000167017c0 = uVar8;
    if (((uVar2 & 6) != 0) && ((_DAT_ffff000016701780 >> 1 & 1) != 0)) {
      FUN_ffff00009077de54(s__USB__QMU__Tx_EP_d_checksum_erro_ffff0000907d5256,1);
      _DAT_ffff000016701780 = uVar3;
    }
  }
  return 1;
}


