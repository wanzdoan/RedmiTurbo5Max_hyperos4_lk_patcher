
void FUN_ffff00009072adb4(void)

{
  int iVar1;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined8 local_28;
  undefined8 uStack_20;
  undefined4 local_18;
  
  local_18 = 0;
  local_28 = 0;
  uStack_20 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_48 = 0;
  uStack_40 = 0;
  FUN_ffff00009077ee50(&local_48,0,0x34);
  iVar1 = FUN_ffff00009072ae44(&local_48);
  if ((-1 < iVar1) && ((int)local_38 == 0x44495341)) {
    FUN_ffff000090719078(s_printk_devkmsg_on_ffff0000907daa39);
    FUN_ffff000090719078(s_initcall_debug_ffff0000907cf557);
    FUN_ffff000090719078(s_loglevel_7_ffff0000907eabc3);
    FUN_ffff000090719078(s_log_buf_len_16M_ffff0000907fd155);
  }
  return;
}


