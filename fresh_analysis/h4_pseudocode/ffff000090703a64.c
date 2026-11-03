
void FUN_ffff000090703a64(void)

{
  int iVar1;
  ulong uVar2;
  long local_30 [4];
  
  uVar2 = FUN_ffff00009075fb2c();
  if ((uVar2 & 1) != 0) {
    FUN_ffff000090794c04();
  }
  FUN_ffff00009078406c();
  FUN_ffff000090716898();
  FUN_ffff000090746004();
  FUN_ffff000090746174(0x1e);
  FUN_ffff000090746154();
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


