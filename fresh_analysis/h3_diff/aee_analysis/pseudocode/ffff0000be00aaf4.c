
void FUN_ffff0000be00aaf4(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be060f54(param_1,s_fullreset_ffff0000be0b3b92,10);
  if (iVar1 == 0) {
    FUN_ffff0000be05fd98(s__FASTBOOT__mrdump_reset__ffff0000be0c5f3e);
    FUN_ffff0000be014ac4(0,0,0);
  }
  DAT_ffff0000be0d6048 = 0;
  FUN_ffff0000be05fd98(s__FASTBOOT__MRDUMP_Dump_Go__ffff0000be0bf324);
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  if (DAT_ffff0000be0f9114 == '\x01') {
    DAT_ffff0000be0f9114 = '\0';
  }
  else {
    DAT_ffff0000be0f9110 = 0;
  }
  FUN_ffff0000be00a980();
  FUN_ffff0000be009d78();
  return;
}


