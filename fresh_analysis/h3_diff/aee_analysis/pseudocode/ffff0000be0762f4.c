
ulong FUN_ffff0000be0762f4
                (int param_1,long param_2,undefined8 param_3,long param_4,undefined8 param_5,
                long param_6,long param_7)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_key____NULL_ffff0000be0b5df0,s_platform_mediatek_mt6991_sboot_c_ffff0000be0ae0cb,
               0x26);
  }
  if (param_4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_in____NULL_ffff0000be0b70a9,s_platform_mediatek_mt6991_sboot_c_ffff0000be0ae0cb,
               0x27);
  }
  if (param_6 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_out____NULL_ffff0000be0c018a,s_platform_mediatek_mt6991_sboot_c_ffff0000be0ae0cb,
               0x28);
  }
  if (param_7 != 0) {
    uVar1 = FUN_ffff0000be075978();
    uVar3 = (ulong)uVar1;
    if (uVar1 == 0) {
      if (*(code **)((&DAT_ffff0000be1b4f88)[param_1] + 200) != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000be0763b8. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        uVar3 = (**(code **)((&DAT_ffff0000be1b4f88)[param_1] + 200))
                          (param_2,param_3,param_4,param_5,param_6,param_7);
        return uVar3;
      }
      lVar2 = FUN_ffff0000be05f1bc(0x178);
      if (lVar2 == 0) {
        uVar3 = 0xd;
      }
      else {
        uVar1 = FUN_ffff0000be076124(lVar2,param_1,param_2,param_3);
        uVar3 = (ulong)uVar1;
        if (uVar1 == 0) {
          uVar1 = FUN_ffff0000be076490(lVar2,param_4,param_5);
          uVar3 = (ulong)uVar1;
          if (uVar1 == 0) {
            uVar3 = FUN_ffff0000be075f10(lVar2,param_6,param_7);
            uVar3 = uVar3 & 0xffffffff;
          }
        }
        FUN_ffff0000be07879c(lVar2,0x178);
        thunk_FUN_ffff0000be09937c(lVar2);
      }
    }
    return uVar3;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be0758b8
            (s_outlen____NULL_ffff0000be0b5de1,s_platform_mediatek_mt6991_sboot_c_ffff0000be0ae0cb,
             0x29);
}


