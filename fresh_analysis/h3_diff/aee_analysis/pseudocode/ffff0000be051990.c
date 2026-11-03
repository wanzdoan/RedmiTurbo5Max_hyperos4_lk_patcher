
void FUN_ffff0000be051990(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff0000be05fd98(s__FASTBOOT__Start_reset_dm_verity_ffff0000be0b140a);
  FUN_ffff0000be0502c0(1);
  if ((DAT_ffff0000be1b7e40 == 0) && (iVar1 = FUN_ffff0000be09a414(), iVar1 != 0)) {
    FUN_ffff0000be05fd98(s__AVB__Failed_to_alloc_memory_for_ffff0000be0c67f6,iVar1);
    pcVar2 = s__AVB__Failed_to_alloc_memory_for_ffff0000be0a9866;
  }
  else {
    iVar1 = FUN_ffff0000be051844();
    if (iVar1 == 0) {
      FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
      return;
    }
    FUN_ffff0000be05fd98(s__AVB__Failed_to_clear_dm_verity_s_ffff0000be0b038d,iVar1);
    pcVar2 = s__AVB__Failed_to_clear_dm_verity_s_ffff0000be0bebeb;
  }
  FUN_ffff0000be0137e8(pcVar2);
  return;
}


