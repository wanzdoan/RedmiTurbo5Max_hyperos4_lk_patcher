
void FUN_ffff0000907143c0(void)

{
  char cVar1;
  int iVar2;
  int iVar3;
  int local_4;

  iVar2 = FUN_ffff00009071617c();
  FUN_ffff00009071071c();
  local_4 = 0;
  cVar1 = FUN_ffff000090710708();
  if (cVar1 == 'c') {
    FUN_ffff0000907871ec(0);
  }
  else {
    FUN_ffff0000907885fc(&local_4);
    if (local_4 == 3) {
      FUN_ffff0000907871ec(1);
    }
    else {
      FUN_ffff000090786a28();
    }
  }
  FUN_ffff000090761494();
  cVar1 = FUN_ffff000090710708();
  if (cVar1 == 'c') {
    FUN_ffff000090765e84();
    FUN_ffff00009070a498();
  }
  else {
    FUN_ffff00009077ea84
              (s__s__d__Notify_boot_linux__ffff0000907cf76d,s_notify_boot_linux_ffff0000907d1183,
               0x3f3);
    FUN_ffff000090716e88(&LAB_ffff000090810130,0);
  }
  iVar3 = FUN_ffff00009071617c();
  FUN_ffff00009077ea84
            (s__PROFILE_______s_takes__d_ms_ffff0000907d0eee,s_mt_boot_init_ffff0000907d9143,
             iVar3 - iVar2);
  return;
}
