// ffff00009071ec80  size=120  FUN_ffff00009071ec80

undefined8 FUN_ffff00009071ec80(void)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 local_4;
  
  local_4 = 0;
  iVar2 = FUN_ffff0000907375fc(&local_4);
  uVar1 = local_4;
  if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_arch_ar_ffff0000907f7e9c,0x1a,
               s__get_rnd__rnd_val__ffff0000907dc47d);
  }
  iVar2 = FUN_ffff0000907375fc(&local_4);
  if (iVar2 == 0) {
    return CONCAT44(uVar1,local_4);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_arch_ar_ffff0000907f7e9c,0x1c,
             s__get_rnd__rnd_val__ffff0000907dc47d);
}


