// ffff000090799b08  size=172  FUN_ffff000090799b08

void FUN_ffff000090799b08(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_hmac____NULL_ffff0000907d2190,s_platform_mediatek_mt6991_sboot_c_ffff0000907d9fe9,
               0x1d);
  }
  if (param_2 != 0) {
    iVar1 = FUN_ffff000090798ff0(*(undefined4 *)(param_1 + 0x70));
    if (iVar1 != 0) {
      return;
    }
                    /* WARNING: Could not recover jumptable at 0xffff000090799b80. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (**(code **)(*(long *)(DAT_ffff000090839a70 + (long)*(int *)(param_1 + 0x70) * 8) + 0xb0))
              (param_1,param_2,param_3);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff000090798f30
            (s_in____NULL_ffff0000907eba3b,s_platform_mediatek_mt6991_sboot_c_ffff0000907d9fe9,0x1e)
  ;
}


