// ffff000090768368  size=340  FUN_ffff000090768368

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090768368(void)

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
  _DAT_ffff00001800d088 = _DAT_ffff00001800d088 & 0xfffffffe;
  iVar1 = FUN_ffff0000907b3730(0);
  if (iVar1 == 0) {
    lVar2 = FUN_ffff0000907c5630();
    if (lVar2 == 0) {
      FUN_ffff00009077de54
                (s___s__Bad_AVB_Operation_Pointer_ffff0000907de9ac,
                 s_clear_dm_verity_status_ffff0000907e82d8);
      iVar1 = 0x100006;
    }
    else {
      iVar1 = FUN_ffff0000907c5f5c(lVar2,s_seccfg_ffff0000907d1c33,0x200);
      if (iVar1 == 0) {
        FUN_ffff00009077eb88(&local_40,0,0x20);
        iVar1 = (**(code **)(lVar2 + 0x68))
                          (lVar2,s_avb_managed_verity_mode_ffff0000907ecf19,0x20,&local_40);
        if (iVar1 != 0) {
          FUN_ffff00009077de54
                    (s___s__Failed_to_write_digest_sha2_ffff0000907eb5d7,
                     s_clear_dm_verity_status_ffff0000907e82d8);
          return iVar1;
        }
        iVar1 = FUN_ffff0000907c5c80(lVar2,s_seccfg_ffff0000907d1c33);
        if (iVar1 == 0) {
          pcVar3 = s___s__Update_digest_sha256_to__s_s_ffff0000907f523d;
        }
        else {
          pcVar3 = s___s__Failed_to_update_digest_sha_ffff0000907ecf31;
        }
      }
      else {
        pcVar3 = s___s__Failed_to_initialize__s__ffff0000907f521e;
      }
      FUN_ffff00009077de54
                (pcVar3,s_clear_dm_verity_status_ffff0000907e82d8,s_seccfg_ffff0000907d1c33);
    }
  }
  else {
    FUN_ffff00009077de54
              (s___s__set_dm_verity_state_fails_0_ffff0000907d9c1e,
               s_clear_dm_verity_status_ffff0000907e82d8,iVar1);
  }
  return iVar1;
}


