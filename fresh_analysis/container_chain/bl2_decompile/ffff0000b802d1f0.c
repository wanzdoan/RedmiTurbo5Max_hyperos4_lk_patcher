
void FUN_ffff0000b802d1f0(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b804a7a4(&DAT_ffff0000b8129bd0);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c
              (s__s_free_VM_failed__ret___d_ffff0000b809f937,s__DICE__ffff0000b8099de7,iVar1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x39,&DAT_ffff0000b8086efb);
  }
  iVar1 = FUN_ffff0000b800d264(param_1);
  if (iVar1 == 0) {
    return;
  }
  FUN_ffff0000b804c33c
            (s__s_free_mblock_failed__ret___d_ffff0000b809e833,s__DICE__ffff0000b8099de7,iVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x3f,&DAT_ffff0000b8086efb);
}


