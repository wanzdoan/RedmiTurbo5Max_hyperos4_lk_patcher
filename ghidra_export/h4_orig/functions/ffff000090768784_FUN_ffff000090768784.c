// ffff000090768784  size=4  FUN_ffff000090768784

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090768784(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff00009077e11c(s__FASTBOOT__Start_reset_dm_verity_ffff0000907e3884);
  FUN_ffff000090766c44(1);
  if ((_DAT_ffff00009090d9d0 == 0) && (iVar1 = FUN_ffff0000907c5cb8(), iVar1 != 0)) {
    FUN_ffff00009077e11c(s__AVB__Failed_to_alloc_memory_for_ffff000090802e14,iVar1);
    pcVar2 = s__AVB__Failed_to_alloc_memory_for_ffff0000907d8469;
  }
  else {
    iVar1 = FUN_ffff000090768638();
    if (iVar1 == 0) {
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      return;
    }
    FUN_ffff00009077e11c(s__AVB__Failed_to_clear_dm_verity_s_ffff0000907e1e3f,iVar1);
    pcVar2 = s__AVB__Failed_to_clear_dm_verity_s_ffff0000907f716b;
  }
  FUN_ffff000090709c9c(pcVar2);
  return;
}


