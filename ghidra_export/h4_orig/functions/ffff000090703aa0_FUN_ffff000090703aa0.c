// ffff000090703aa0  size=188  FUN_ffff000090703aa0

void FUN_ffff000090703aa0(void)

{
  int iVar1;
  long local_30 [4];
  
  FUN_ffff000090788fe8();
  thunk_FUN_ffff000090798f80();
  FUN_ffff000090722a30(0);
  FUN_ffff000090722a30(1);
  FUN_ffff000090722a30(2);
  FUN_ffff000090722a30(3);
  FUN_ffff000090722a30(4);
  FUN_ffff000090722a30(5);
  FUN_ffff000090722a30(6);
  iVar1 = FUN_ffff0000907255d8();
  if (iVar1 != 0) {
    local_30[2] = 0;
    local_30[3] = 0;
    local_30[0] = 0;
    local_30[1] = 0;
    FUN_ffff00009071ebf4(0x8200041f,1,0,0,0,0,0,0,local_30);
    if (local_30[0] != 0) {
      FUN_ffff00009077e11c
                (s__s__devapc_control_failed__ffff0000907fe898,
                 s_platform_sec_post_init_ffff0000907d08ae);
    }
  }
  return;
}


