// ffff000090703b28  size=188  FUN_ffff000090703b28

void FUN_ffff000090703b28(void)

{
  int iVar1;
  long local_30 [4];
  
  FUN_ffff000090788d28();
  thunk_FUN_ffff000090798cc0();
  FUN_ffff0000907227ec(0);
  FUN_ffff0000907227ec(1);
  FUN_ffff0000907227ec(2);
  FUN_ffff0000907227ec(3);
  FUN_ffff0000907227ec(4);
  FUN_ffff0000907227ec(5);
  FUN_ffff0000907227ec(6);
  iVar1 = FUN_ffff000090725394();
  if (iVar1 != 0) {
    local_30[2] = 0;
    local_30[3] = 0;
    local_30[0] = 0;
    local_30[1] = 0;
    FUN_ffff00009071ea6c(0x8200041f,1,0,0,0,0,0,0,local_30);
    if (local_30[0] != 0) {
      FUN_ffff00009077de54
                (s__s__devapc_control_failed__ffff0000907fe6f0,
                 s_platform_sec_post_init_ffff0000907d088f);
    }
  }
  return;
}


