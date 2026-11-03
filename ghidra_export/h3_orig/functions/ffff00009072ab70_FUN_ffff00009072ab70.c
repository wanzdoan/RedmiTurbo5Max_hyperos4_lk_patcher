// ffff00009072ab70  size=144  FUN_ffff00009072ab70

void FUN_ffff00009072ab70(void)

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
  FUN_ffff00009077eb88(&local_48,0,0x34);
  iVar1 = FUN_ffff00009072ac00(&local_48);
  if ((-1 < iVar1) && ((int)local_38 == 0x44495341)) {
    FUN_ffff000090718f34(s_printk_devkmsg_on_ffff0000907da92e);
    FUN_ffff000090718f34(s_initcall_debug_ffff0000907cf538);
    FUN_ffff000090718f34(s_loglevel_7_ffff0000907eaa40);
    FUN_ffff000090718f34(s_log_buf_len_16M_ffff0000907fcfad);
  }
  return;
}


