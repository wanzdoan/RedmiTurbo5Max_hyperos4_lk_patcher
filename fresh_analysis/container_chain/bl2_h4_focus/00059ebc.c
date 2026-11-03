// File offset entry: 0x59ebc

bool FUN_ffff0000b8059ebc(void)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  undefined4 local_24;
  
  lVar3 = FUN_ffff0000b8056e5c(0);
  local_24 = 0;
  FUN_ffff0000b800ad90(lVar3 + 0x270,0xffffffff);
  iVar2 = FUN_ffff0000b8059298();
  if (iVar2 != 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81b6b80,0,0x10);
  }
  iVar2 = FUN_ffff0000b8058e5c(0,0,&DAT_ffff0000b81b6b80,0x10,&local_24);
  FUN_ffff0000b800ae28(lVar3 + 0x270);
  bVar1 = iVar2 - 1U < 0xfffffffe;
  FUN_ffff0000b804c33c
            (s___s___RPMB_setkey_flag__d_ffff0000b809fe54,
             s_ufs_rpmb_get_setkey_flag_ffff0000b808a3fd,bVar1);
  return bVar1;
}


