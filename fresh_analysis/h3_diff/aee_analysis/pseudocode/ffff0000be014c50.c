
void FUN_ffff0000be014c50(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be0bac04);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be01e59c(&DAT_ffff0000be0a6a89);
    if (iVar1 == 0) {
LAB_ffff0000be014cd8:
      FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
      return;
    }
    pcVar2 = s_set_slot_a_failed__ffff0000be0a8ddf;
  }
  else {
    iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be0a6a8c);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000be01e59c(&DAT_ffff0000be0b863d);
      if (iVar1 == 0) goto LAB_ffff0000be014cd8;
      pcVar2 = s_set_slot_b_failed__ffff0000be0af5e1;
    }
    else {
      pcVar2 = s_command_parameter_is_not_allowed_ffff0000be0a43de;
    }
  }
  FUN_ffff0000be0137e8(pcVar2);
  return;
}


