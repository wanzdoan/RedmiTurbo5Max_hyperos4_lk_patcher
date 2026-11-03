
void FUN_ffff0000be076490(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_hmac____NULL_ffff0000be0a526b,s_platform_mediatek_mt6991_sboot_c_ffff0000be0aaec7,
               0x1d);
  }
  if (param_2 != 0) {
    iVar1 = FUN_ffff0000be075978(*(undefined4 *)(param_1 + 0x70));
    if (iVar1 != 0) {
      return;
    }
                    /* WARNING: Could not recover jumptable at 0xffff0000be076508. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (**(code **)(*(long *)(PTR_DAT_ffff0000be0f5710 + (long)*(int *)(param_1 + 0x70) * 8) + 0xb0))
              (param_1,param_2,param_3);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be0758b8
            (s_in____NULL_ffff0000be0b70a9,s_platform_mediatek_mt6991_sboot_c_ffff0000be0aaec7,0x1e)
  ;
}


