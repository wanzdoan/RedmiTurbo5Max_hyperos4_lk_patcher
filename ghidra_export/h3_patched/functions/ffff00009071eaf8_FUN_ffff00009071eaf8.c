// ffff00009071eaf8  size=120  FUN_ffff00009071eaf8

undefined8 FUN_ffff00009071eaf8(void)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 local_4;
  
  local_4 = 0;
  iVar2 = FUN_ffff000090737364(&local_4);
  uVar1 = local_4;
  if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_arch_ar_ffff0000907f7d00,0x1a,
               s__get_rnd__rnd_val__ffff0000907dc390);
  }
  iVar2 = FUN_ffff000090737364(&local_4);
  if (iVar2 == 0) {
    return CONCAT44(uVar1,local_4);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_arch_ar_ffff0000907f7d00,0x1c,
             s__get_rnd__rnd_val__ffff0000907dc390);
}


