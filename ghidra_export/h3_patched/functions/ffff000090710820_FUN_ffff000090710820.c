// ffff000090710820  size=180  FUN_ffff000090710820

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090710820(void)

{
  int iVar1;
  int *piVar2;
  uint *puVar3;
  uint uVar4;
  uint uVar5;
  
  if ((DAT_ffff00009083f600 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x1e4,
               s_g_boot_info_hdr_loaded_ffff0000907d25a2);
  }
  if ((DAT_ffff00009083f608 & 1) != 0) {
    piVar2 = (int *)&DAT_ffff00009083e10c;
    if (_DAT_ffff00009083e758 < 4) {
      piVar2 = (int *)&DAT_ffff00009083e740;
    }
    puVar3 = (uint *)&DAT_ffff00009083edbc;
    if (_DAT_ffff00009083e758 < 3) {
      puVar3 = (uint *)&DAT_ffff00009083e754;
    }
    iVar1 = _DAT_ffff00009083edc8;
    if (_DAT_ffff00009083e758 < 3) {
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
  FUN_ffff00009077cd04
            (s_app_mt_boot_boot_info_c_ffff0000907e0ab1,0x1b6,
             s_g_boot_info_img_loaded_ffff0000907f0c0d);
}


