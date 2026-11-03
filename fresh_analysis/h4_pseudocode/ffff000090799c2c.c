
ulong FUN_ffff000090799c2c
                (int param_1,long param_2,undefined8 param_3,long param_4,undefined8 param_5,
                long param_6,long param_7)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_key____NULL_ffff0000907ea34b,s_platform_mediatek_mt6991_sboot_c_ffff0000907defe8,
               0x26);
  }
  if (param_4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_in____NULL_ffff0000907ebbd5,s_platform_mediatek_mt6991_sboot_c_ffff0000907defe8,
               0x27);
  }
  if (param_6 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_out____NULL_ffff0000907f9090,s_platform_mediatek_mt6991_sboot_c_ffff0000907defe8,
               0x28);
  }
  if (param_7 != 0) {
    uVar1 = FUN_ffff0000907992b0();
    uVar3 = (ulong)uVar1;
    if (uVar1 == 0) {
      if (*(code **)((&DAT_ffff00009090aa78)[param_1] + 200) != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff000090799cf0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        uVar3 = (**(code **)((&DAT_ffff00009090aa78)[param_1] + 200))
                          (param_2,param_3,param_4,param_5,param_6,param_7);
        return uVar3;
      }
      lVar2 = FUN_ffff00009077d514(0x178);
      if (lVar2 == 0) {
        uVar3 = 0xd;
      }
      else {
        uVar1 = FUN_ffff000090799a5c(lVar2,param_1,param_2,param_3);
        uVar3 = (ulong)uVar1;
        if (uVar1 == 0) {
          uVar1 = FUN_ffff000090799dc8(lVar2,param_4,param_5);
          uVar3 = (ulong)uVar1;
          if (uVar1 == 0) {
            uVar3 = FUN_ffff000090799848(lVar2,param_6,param_7);
            uVar3 = uVar3 & 0xffffffff;
          }
        }
        FUN_ffff00009079c0d4(lVar2,0x178);
        thunk_FUN_ffff0000907c314c(lVar2);
      }
    }
    return uVar3;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000907991f0
            (s_outlen____NULL_ffff0000907ea33c,s_platform_mediatek_mt6991_sboot_c_ffff0000907defe8,
             0x29);
}


