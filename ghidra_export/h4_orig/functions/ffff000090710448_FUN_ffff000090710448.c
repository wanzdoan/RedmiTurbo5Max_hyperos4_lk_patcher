// ffff000090710448  size=232  FUN_ffff000090710448

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090710448(int param_1,undefined4 *param_2,undefined4 *param_3)

{
  int *piVar1;
  uint uVar2;
  uint uVar3;
  uint *puVar4;
  int iVar5;
  undefined8 uVar6;
  ulong uVar7;
  long lVar8;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x124,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x125,
               s_g_boot_info_img_loaded_ffff0000907f0d95);
  }
  piVar1 = (int *)&DAT_ffff00009083f61c;
  puVar4 = (uint *)&DAT_ffff00009083edf4;
  if (_DAT_ffff00009083e790 < 3) {
    piVar1 = (int *)&DAT_ffff00009083edd8;
    puVar4 = (uint *)&DAT_ffff00009083e78c;
  }
  uVar7 = (ulong)(_DAT_ffff00009083f628 >> 2) / 0x1b & 0xff;
  if ((int)uVar7 != 0) {
    uVar2 = *puVar4;
    uVar3 = 0;
    if (uVar2 != 0) {
      uVar3 = ((_DAT_ffff00009083ee00 + uVar2) - 1) / uVar2;
    }
    lVar8 = _DAT_ffff00009083f650 + (ulong)uVar2 + (long)(int)(uVar3 * uVar2) +
            (ulong)((uVar2 + *piVar1) - 1 & -uVar2) + 0x2c;
    do {
      if (*(int *)(lVar8 + -0x24) == param_1) {
        if (DAT_ffff000090839938 != 0) {
          uVar6 = FUN_ffff000090710550();
          iVar5 = FUN_ffff00009077f0ac(uVar6,lVar8);
          if (iVar5 == 0) {
            FUN_ffff00009077e11c
                      (s__s__d__board_id_in_vendor_ramdis_ffff0000907f401e,
                       s_traverse_vendor_ramdisk_entry_ffff0000907d8cbf,0x138,lVar8);
            FUN_ffff000090710530();
            return;
          }
        }
        *param_3 = *(undefined4 *)(lVar8 + -0x2c);
        *param_2 = *(undefined4 *)(lVar8 + -0x28);
        return;
      }
      lVar8 = lVar8 + 0x6c;
      uVar7 = uVar7 - 1;
    } while (uVar7 != 0);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x141,s_idx_<_entry_cnt_ffff0000907ea6fb);
}


