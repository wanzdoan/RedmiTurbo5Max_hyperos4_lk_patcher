
void FUN_ffff0000be024768(void)

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
  FUN_ffff0000be060ae0(&local_48,0,0x34);
  iVar1 = FUN_ffff0000be0247f8(&local_48);
  if ((-1 < iVar1) && ((int)local_38 == 0x44495341)) {
    FUN_ffff0000be01bb50(s_printk_devkmsg_on_ffff0000be0ab594);
    FUN_ffff0000be01bb50(s_initcall_debug_ffff0000be0a33f2);
    FUN_ffff0000be01bb50(s_loglevel_7_ffff0000be0b642f);
    FUN_ffff0000be01bb50(s_log_buf_len_16M_ffff0000be0c2b91);
  }
  return;
}


