
int FUN_ffff0000907694b4(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  
  uVar2 = FUN_ffff00009077f250();
  pcVar3 = s_androidboot_vbmeta_hash_alg_ffff0000907e9e75;
  iVar1 = FUN_ffff0000907c5ec8(param_1,uVar2,s_androidboot_vbmeta_hash_alg_ffff0000907e9e75);
  if (iVar1 == 0) {
    uVar2 = FUN_ffff00009077f250(param_1);
    pcVar3 = s_androidboot_vbmeta_size_ffff0000907fe15c;
    iVar1 = FUN_ffff0000907c5ec8(param_1,uVar2,s_androidboot_vbmeta_size_ffff0000907fe15c);
    if (iVar1 == 0) {
      uVar2 = FUN_ffff00009077f250(param_1);
      pcVar3 = s_androidboot_vbmeta_digest_ffff0000907e9e91;
      iVar1 = FUN_ffff0000907c5ec8(param_1,uVar2,s_androidboot_vbmeta_digest_ffff0000907e9e91);
      if (iVar1 == 0) {
        return 0;
      }
    }
  }
  FUN_ffff00009077e11c(s__AVB__Extract__s_fails_ffff0000907d8493,pcVar3);
  return iVar1;
}


