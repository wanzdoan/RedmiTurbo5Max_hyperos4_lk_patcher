
void FUN_ffff0000b802d290(void)

{
  int iVar1;
  long lVar2;
  undefined8 local_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_48 = 0;
  lVar2 = FUN_ffff0000b802d110(s_security_dice_pvmfw_ffff0000b80a126d,&local_48);
  if (lVar2 != 0) {
    FUN_ffff0000b804d070(lVar2,0xf,0x1000);
    FUN_ffff0000b804d070(lVar2,0,0x1000);
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    iVar1 = thunk_FUN_ffff0000b8061a14(lVar2,0x1000,&local_40);
    if (iVar1 != 0) {
LAB_ffff0000b802d3bc:
      FUN_ffff0000b804c33c
                (s__s_sec_sha256_adapter_failed__re_ffff0000b80a05aa,s__DICE__ffff0000b8099de7,iVar1
                );
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x52,&DAT_ffff0000b8086efb);
    }
    iVar1 = FUN_ffff0000b804cd08(&local_40,&DAT_ffff0000b80a56f0,0x20);
    if (iVar1 == 0) {
      FUN_ffff0000b802d1f0(local_48,lVar2);
      lVar2 = FUN_ffff0000b802d110(s_security_dice_mtee_ffff0000b8098f50,&local_48);
      if (lVar2 == 0) goto LAB_ffff0000b802d3a4;
      FUN_ffff0000b804d070(lVar2,0xf,0x1000);
      FUN_ffff0000b804d070(lVar2,0,0x1000);
      local_30 = 0;
      uStack_28 = 0;
      local_40 = 0;
      uStack_38 = 0;
      iVar1 = thunk_FUN_ffff0000b8061a14(lVar2,0x1000,&local_40);
      if (iVar1 != 0) goto LAB_ffff0000b802d3bc;
      iVar1 = FUN_ffff0000b804cd08(&local_40,&DAT_ffff0000b80a56f0,0x20);
      if (iVar1 == 0) {
        FUN_ffff0000b802d1f0(local_48,lVar2);
        return;
      }
    }
    FUN_ffff0000b804c33c
              (s__s_comparison_failed___d__mblock_ffff0000b809c921,s__DICE__ffff0000b8099de7,iVar1);
    FUN_ffff0000b804ba54(lVar2,0x1000,0);
    FUN_ffff0000b804c33c(s__s_hash__ffff0000b809f953,s__DICE__ffff0000b8099de7);
    FUN_ffff0000b804ba54(&local_40,0x20,0);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x5e,&DAT_ffff0000b8086efb);
  }
LAB_ffff0000b802d3a4:
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x6b,&DAT_ffff0000b8086efb);
}


