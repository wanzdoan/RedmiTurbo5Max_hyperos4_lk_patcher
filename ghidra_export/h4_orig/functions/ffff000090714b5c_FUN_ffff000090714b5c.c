// ffff000090714b5c  size=212  FUN_ffff000090714b5c

void FUN_ffff000090714b5c(void)

{
  char cVar1;
  int iVar2;
  int iVar3;
  int local_4;
  
  iVar2 = FUN_ffff000090716918();
  FUN_ffff000090710bd4();
  local_4 = 0;
  cVar1 = FUN_ffff000090710bc0();
  if (cVar1 == 'c') {
    FUN_ffff0000907869a0(0);
  }
  else {
    FUN_ffff000090788a10(&local_4);
    if (local_4 == 3) {
      FUN_ffff0000907869a0(1);
    }
    else {
      FUN_ffff0000907861dc();
    }
  }
  FUN_ffff0000907603f8();
  cVar1 = FUN_ffff000090710bc0();
  if (cVar1 == 'c') {
    FUN_ffff000090764de8();
    FUN_ffff00009070a3e4();
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__Notify_boot_linux__ffff0000907d752b,s_notify_boot_linux_ffff0000907d8dc1,
               0x408);
    FUN_ffff000090717624(&LAB_ffff000090818168,0);
  }
  iVar3 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s__PROFILE_______s_takes__d_ms_ffff0000907d8b16,s_mt_boot_init_ffff0000907e0c12,
             iVar3 - iVar2);
  return;
}


