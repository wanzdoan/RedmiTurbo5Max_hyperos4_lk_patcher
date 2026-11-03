
void FUN_ffff0000be015c4c(void)

{
  int iVar1;
  undefined8 uVar2;
  int local_4;
  
  local_4 = 0;
  iVar1 = thunk_FUN_ffff0000be0906c4(&local_4);
  if (iVar1 == 0) {
    uVar2 = FUN_ffff0000be013604(s_secure_ffff0000be0a581c);
    iVar1 = FUN_ffff0000be060cb8(uVar2,&DAT_ffff0000be0ae340);
    if (iVar1 == 0) {
      FUN_ffff0000be013510(s_secure_ffff0000be0a581c,(long)local_4 * 4 + -0xffff41f38890);
    }
    else {
      FUN_ffff0000be0135a4(s_secure_ffff0000be0a581c);
    }
    uVar2 = FUN_ffff0000be013604(s_unlocked_ffff0000be0be0a2);
    iVar1 = FUN_ffff0000be060cb8(uVar2,&DAT_ffff0000be0ae340);
    if (iVar1 == 0) {
      FUN_ffff0000be013510(s_unlocked_ffff0000be0be0a2,(ulong)(local_4 == 0) * 4 + -0xffff41f38890);
    }
    else {
      FUN_ffff0000be0135a4(s_unlocked_ffff0000be0be0a2);
    }
  }
  else {
    FUN_ffff0000be0137e8(s_Failed_to_get_secure_status__ffff0000be0aa212);
  }
  return;
}


