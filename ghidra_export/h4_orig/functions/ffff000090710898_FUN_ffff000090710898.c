// ffff000090710898  size=180  FUN_ffff000090710898

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090710898(void)

{
  int iVar1;
  int *piVar2;
  uint *puVar3;
  uint uVar4;
  uint uVar5;
  
  if ((DAT_ffff00009083f638 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1e4,
               s_g_boot_info_hdr_loaded_ffff0000907d25df);
  }
  if ((DAT_ffff00009083f640 & 1) != 0) {
    piVar2 = (int *)&DAT_ffff00009083e144;
    if (_DAT_ffff00009083e790 < 4) {
      piVar2 = (int *)&DAT_ffff00009083e778;
    }
    puVar3 = (uint *)&DAT_ffff00009083edf4;
    if (_DAT_ffff00009083e790 < 3) {
      puVar3 = (uint *)&DAT_ffff00009083e78c;
    }
    iVar1 = _DAT_ffff00009083ee00;
    if (_DAT_ffff00009083e790 < 3) {
      iVar1 = 0;
    }
    uVar4 = *puVar3;
    uVar5 = 0;
    if (uVar4 != 0) {
      uVar5 = ((iVar1 + *piVar2 + uVar4) - 1) / uVar4;
    }
    return uVar5 * uVar4;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_app_mt_boot_boot_info_c_ffff0000907e0b6b,0x1b6,
             s_g_boot_info_img_loaded_ffff0000907f0d95);
}


