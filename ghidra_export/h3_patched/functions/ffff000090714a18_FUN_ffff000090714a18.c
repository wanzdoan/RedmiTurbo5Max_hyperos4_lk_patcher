// ffff000090714a18  size=212  FUN_ffff000090714a18

void FUN_ffff000090714a18(void)

{
  char cVar1;
  int iVar2;
  int iVar3;
  int local_4;
  
  iVar2 = FUN_ffff0000907167d4();
  FUN_ffff000090710b5c();
  local_4 = 0;
  cVar1 = FUN_ffff000090710b48();
  if (cVar1 == 'c') {
    FUN_ffff0000907866d8(0);
  }
  else {
    FUN_ffff000090788750(&local_4);
    if (local_4 == 3) {
      FUN_ffff0000907866d8(1);
    }
    else {
      FUN_ffff000090785f14();
    }
  }
  FUN_ffff000090760160();
  cVar1 = FUN_ffff000090710b48();
  if (cVar1 == 'c') {
    FUN_ffff000090764b50();
    FUN_ffff00009070a458();
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__Notify_boot_linux__ffff0000907d7495,s_notify_boot_linux_ffff0000907d8d2a,
               0x3f3);
    FUN_ffff0000907174e0(&LAB_ffff000090818158,0);
  }
  iVar3 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,s_mt_boot_init_ffff0000907e0b58,
             iVar3 - iVar2);
  return;
}


