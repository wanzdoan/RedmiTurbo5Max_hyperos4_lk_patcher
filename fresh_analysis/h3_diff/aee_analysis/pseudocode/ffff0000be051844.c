
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be051844(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  _DAT_ffff00001800d088 = 0;
  iVar1 = FUN_ffff0000be08dea0(0);
  if (iVar1 == 0) {
    lVar2 = FUN_ffff0000be09a400();
    if (lVar2 == 0) {
      FUN_ffff0000be05fd98
                (s___s__Bad_AVB_Operation_Pointer_ffff0000be0add68,
                 s_clear_dm_verity_status_ffff0000be0b48b7);
      iVar1 = 0x100006;
    }
    else {
      iVar1 = FUN_ffff0000be09a7c8(lVar2,s_seccfg_ffff0000be0a4fd5,0x200);
      if (iVar1 == 0) {
        FUN_ffff0000be060ae0(&local_40,0,0x20);
        iVar1 = (**(code **)(lVar2 + 0x68))
                          (lVar2,s_avb_managed_verity_mode_ffff0000be0b7eab,0x20,&local_40);
        if (iVar1 != 0) {
          FUN_ffff0000be05fd98
                    (s___s__Failed_to_write_digest_sha2_ffff0000be0b6d9f,
                     s_clear_dm_verity_status_ffff0000be0b48b7);
          return iVar1;
        }
        iVar1 = FUN_ffff0000be09a4ec(lVar2,s_seccfg_ffff0000be0a4fd5);
        if (iVar1 == 0) {
          pcVar3 = s___s__Update_digest_sha256_to__s_s_ffff0000be0bd8c6;
        }
        else {
          pcVar3 = s___s__Failed_to_update_digest_sha_ffff0000be0b7ec3;
        }
      }
      else {
        pcVar3 = s___s__Failed_to_initialize__s__ffff0000be0bd8a7;
      }
      FUN_ffff0000be05fd98
                (pcVar3,s_clear_dm_verity_status_ffff0000be0b48b7,s_seccfg_ffff0000be0a4fd5);
    }
  }
  else {
    FUN_ffff0000be05fd98
              (s___s__set_dm_verity_state_fails_0_ffff0000be0aabc1,
               s_clear_dm_verity_status_ffff0000be0b48b7,iVar1);
  }
  return iVar1;
}


