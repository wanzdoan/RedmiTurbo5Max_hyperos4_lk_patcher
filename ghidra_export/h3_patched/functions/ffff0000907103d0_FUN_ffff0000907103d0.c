// ffff0000907103d0  size=232  FUN_ffff0000907103d0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907103d0(int param_1,undefined4 *param_2,undefined4 *param_3)

{
  int *piVar1;
  uint uVar2;
  uint uVar3;
  uint *puVar4;
  int iVar5;
  undefined8 uVar6;
  ulong uVar7;
  long lVar8;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x124,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x125,
               s_g_boot_info_img_loaded_ffff0000907f0c0d);
  }
  piVar1 = (int *)&DAT_ffff00009083f5e4;
  puVar4 = (uint *)&DAT_ffff00009083edbc;
  if (_DAT_ffff00009083e758 < 3) {
    piVar1 = (int *)&DAT_ffff00009083eda0;
    puVar4 = (uint *)&DAT_ffff00009083e754;
  }
  uVar7 = (ulong)(_DAT_ffff00009083f5f0 >> 2) / 0x1b & 0xff;
  if ((int)uVar7 != 0) {
    uVar2 = *puVar4;
    uVar3 = 0;
    if (uVar2 != 0) {
      uVar3 = ((_DAT_ffff00009083edc8 + uVar2) - 1) / uVar2;
    }
    lVar8 = _DAT_ffff00009083f618 + (ulong)uVar2 + (long)(int)(uVar3 * uVar2) +
            (ulong)((uVar2 + *piVar1) - 1 & -uVar2) + 0x2c;
    do {
      if (*(int *)(lVar8 + -0x24) == param_1) {
        if (DAT_ffff000090839918 != 0) {
          uVar6 = FUN_ffff0000907104d8();
          iVar5 = FUN_ffff00009077ede4(uVar6,lVar8);
          if (iVar5 == 0) {
            FUN_ffff00009077de54
                      (s__s__d__board_id_in_vendor_ramdis_ffff0000907f3e7b,
                       s_traverse_vendor_ramdisk_entry_ffff0000907d8c28,0x138,lVar8);
            FUN_ffff0000907104b8();
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
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x141,s_idx_<_entry_cnt_ffff0000907ea58a);
}


