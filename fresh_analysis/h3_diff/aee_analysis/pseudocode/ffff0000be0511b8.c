
void FUN_ffff0000be0511b8(void)

{
  uint uVar1;
  char *pcVar2;
  
  uVar1 = FUN_ffff0000be065704();
  if (uVar1 < 0x14) {
    pcVar2 = &DAT_ffff0000be0cc810 + *(int *)(&DAT_ffff0000be0cc810 + (long)(int)uVar1 * 4);
  }
  else {
    pcVar2 = s_unknown_error_ffff0000be0b48a9;
  }
  FUN_ffff0000be013738(pcVar2);
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  return;
}


