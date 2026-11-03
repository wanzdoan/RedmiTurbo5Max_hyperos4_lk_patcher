// ffff00009079996c  size=412  FUN_ffff00009079996c

ulong FUN_ffff00009079996c
                (int param_1,long param_2,undefined8 param_3,long param_4,undefined8 param_5,
                long param_6,long param_7)

{
  uint uVar1;
  long lVar2;
  code *UNRECOVERED_JUMPTABLE;
  ulong uVar3;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_key____NULL_ffff0000907ea207,s_platform_mediatek_mt6991_sboot_c_ffff0000907def45,
               0x26);
  }
  if (param_4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_in____NULL_ffff0000907eba52,s_platform_mediatek_mt6991_sboot_c_ffff0000907def45,
               0x27);
  }
  if (param_6 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_out____NULL_ffff0000907f8ef4,s_platform_mediatek_mt6991_sboot_c_ffff0000907def45,
               0x28);
  }
  if (param_7 != 0) {
    uVar1 = FUN_ffff000090798ff0();
    uVar3 = (ulong)uVar1;
    if (uVar1 == 0) {
      UNRECOVERED_JUMPTABLE = *(code **)(*(long *)((long)param_1 * 8 + -0xffff6f6f5648) + 200);
      if (UNRECOVERED_JUMPTABLE != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff000090799a30. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        uVar3 = (*UNRECOVERED_JUMPTABLE)(param_2,param_3,param_4,param_5,param_6,param_7);
        return uVar3;
      }
      lVar2 = FUN_ffff00009077d24c(0x178);
      if (lVar2 == 0) {
        uVar3 = 0xd;
      }
      else {
        uVar1 = FUN_ffff00009079979c(lVar2,param_1,param_2,param_3);
        uVar3 = (ulong)uVar1;
        if (uVar1 == 0) {
          uVar1 = FUN_ffff000090799b08(lVar2,param_4,param_5);
          uVar3 = (ulong)uVar1;
          if (uVar1 == 0) {
            uVar3 = FUN_ffff000090799588(lVar2,param_6,param_7);
            uVar3 = uVar3 & 0xffffffff;
          }
        }
        FUN_ffff00009079be14(lVar2,0x178);
        thunk_FUN_ffff0000907c2ad8(lVar2);
      }
    }
    return uVar3;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff000090798f30
            (s_outlen____NULL_ffff0000907ea1f8,s_platform_mediatek_mt6991_sboot_c_ffff0000907def45,
             0x29);
}


