
undefined8 FUN_ffff0000b800e798(void)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 local_4;
  
  local_4 = 0;
  iVar2 = FUN_ffff0000b80212b4(&local_4);
  uVar1 = local_4;
  if (iVar2 != 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_arch_ar_ffff0000b809b574,0x1a,
               s__get_rnd__rnd_val__ffff0000b808c3a3);
  }
  iVar2 = FUN_ffff0000b80212b4(&local_4);
  if (iVar2 == 0) {
    return CONCAT44(uVar1,local_4);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_common_arch_ar_ffff0000b809b574,0x1c,
             s__get_rnd__rnd_val__ffff0000b808c3a3);
}


