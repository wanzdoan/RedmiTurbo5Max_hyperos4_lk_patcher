
undefined8
FUN_ffff0000b803e004
          (undefined8 *param_1,undefined4 param_2,undefined4 param_3,undefined4 param_4,
          undefined4 param_5)

{
  uint *puVar1;
  code *pcVar2;
  
  if (((param_1 != (undefined8 *)0x0) && (puVar1 = (uint *)*param_1, puVar1 != (uint *)0x0)) &&
     (param_1[1] != 0)) {
    pcVar2 = *(code **)(param_1[1] + 0x78);
    if (pcVar2 != (code *)0x0) {
      (*pcVar2)(param_2,param_3,param_4,param_5);
      return 0;
    }
    if ((1 < *puVar1) && ((*puVar1 != 2 || (3 < puVar1[0x5c])))) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_disp_di_ffff0000b808cb1c,0x475,&DAT_ffff0000b8086efb);
    }
  }
  return 0xffffffff;
}


