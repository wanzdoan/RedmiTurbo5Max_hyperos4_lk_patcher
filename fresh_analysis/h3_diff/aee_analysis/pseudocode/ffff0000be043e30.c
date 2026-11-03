
undefined8 FUN_ffff0000be043e30(int param_1,long param_2)

{
  long lVar1;
  ulong uVar2;
  
  if (param_1 == 0xef) {
    uVar2 = 0;
  }
  else {
    if (param_1 != 0xf0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_mt6991_disp_dd_ffff0000be0b2318,0x21,&DAT_ffff0000be0a553d);
    }
    uVar2 = 0x400000;
  }
  lVar1 = 0x928;
  if (*(char *)(param_2 + 0x2d8) != '\0') {
    lVar1 = 0x2c8;
  }
  *(uint *)(uVar2 | 0xffff000032970030) =
       *(uint *)(param_2 + lVar1) | *(int *)(param_2 + 0x92c) << 0x10;
  *(undefined4 *)(uVar2 | 0xffff000032970010) = 0x4000001;
  *(undefined4 *)(uVar2 | 0xffff000032970020) = 1;
  return 0;
}


