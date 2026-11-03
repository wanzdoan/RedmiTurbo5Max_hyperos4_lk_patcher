
undefined8 FUN_ffff0000be0787cc(undefined1 *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  iVar1 = DAT_ffff0000be0f3e50;
  if (param_1 != (undefined1 *)0x0) {
    if (DAT_ffff0000be0f3e50 != 0) {
      lVar2 = FUN_ffff0000be05f250(DAT_ffff0000be0f3e50,2);
      *(long *)(param_1 + 0x10) = lVar2;
      if (lVar2 == 0) {
        uVar3 = 0xfffffffe;
      }
      else {
        uVar3 = 0;
        *param_1 = 0;
        *(int *)(param_1 + 4) = iVar1;
        *(undefined4 *)(param_1 + 8) = 1;
      }
      return uVar3;
    }
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_sboot_c_ffff0000be0af189,0x137,
             s_mp______void__0_____prec_>_0_ffff0000be0b5dfc);
}


