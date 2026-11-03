
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090768638(void)

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
  iVar1 = FUN_ffff0000907b3da0(0);
  if (iVar1 == 0) {
    lVar2 = FUN_ffff0000907c5ca4();
    if (lVar2 == 0) {
      FUN_ffff00009077e11c
                (s___s__Bad_AVB_Operation_Pointer_ffff0000907dea66,
                 s_clear_dm_verity_status_ffff0000907e8433);
      iVar1 = 0x100006;
    }
    else {
      iVar1 = FUN_ffff0000907c65d0(lVar2,s_seccfg_ffff0000907d1c70,0x200);
      if (iVar1 == 0) {
        FUN_ffff00009077ee50(&local_40,0,0x20);
        iVar1 = (**(code **)(lVar2 + 0x68))
                          (lVar2,s_avb_managed_verity_mode_ffff0000907ed0b3,0x20,&local_40);
        if (iVar1 != 0) {
          FUN_ffff00009077e11c
                    (s___s__Failed_to_write_digest_sha2_ffff0000907eb771,
                     s_clear_dm_verity_status_ffff0000907e8433);
          return iVar1;
        }
        iVar1 = FUN_ffff0000907c62f4(lVar2,s_seccfg_ffff0000907d1c70);
        if (iVar1 == 0) {
          pcVar3 = s___s__Update_digest_sha256_to__s_s_ffff0000907f53c1;
        }
        else {
          pcVar3 = s___s__Failed_to_update_digest_sha_ffff0000907ed0cb;
        }
      }
      else {
        pcVar3 = s___s__Failed_to_initialize__s__ffff0000907f53a2;
      }
      FUN_ffff00009077e11c
                (pcVar3,s_clear_dm_verity_status_ffff0000907e8433,s_seccfg_ffff0000907d1c70);
    }
  }
  else {
    FUN_ffff00009077e11c
              (s___s__set_dm_verity_state_fails_0_ffff0000907d9cb5,
               s_clear_dm_verity_status_ffff0000907e8433,iVar1);
  }
  return iVar1;
}


