
void FUN_ffff0000b8051b40(void)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined8 uVar4;
  ulong uVar5;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  long local_50;
  long local_48;
  uint *local_40;
  undefined8 local_38;
  long local_30;
  undefined8 local_28;
  
  local_30 = 0;
  local_28 = 0;
  local_40 = (uint *)0x0;
  local_38 = 0;
  local_50 = 0;
  local_48 = 0;
  iVar1 = FUN_ffff0000b805159c(&local_50);
  if (iVar1 == 0) {
    FUN_ffff0000b8051824(&DAT_ffff0000b8133de0);
    DAT_ffff0000b8133dac = 1;
    if (local_40 != (uint *)0x0) {
      DAT_ffff0000b8133dac = (uint)(5 < *local_40);
    }
    uVar2 = FUN_ffff0000b8082b84(3);
    uVar4 = FUN_ffff0000b803e898();
    local_70 = DAT_ffff0000b8133df0;
    uStack_68 = DAT_ffff0000b8133df8;
    local_60 = DAT_ffff0000b8133e00;
    uStack_58 = DAT_ffff0000b8133e08;
    local_80 = DAT_ffff0000b8133de0;
    uStack_78 = DAT_ffff0000b8133de8;
    FUN_ffff0000b8082d3c(uVar2,uVar4,local_38,local_40,&local_80,0xffffffff);
    uVar5 = FUN_ffff0000b8051340();
    if (((uVar5 & 1) == 0) || (iVar1 = FUN_ffff0000b80519c0(&local_50), iVar1 != 0)) {
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b803e8ec(0,0,uVar2,uVar3);
    }
    else {
      FUN_ffff0000b8051a90(&DAT_ffff0000b8133e10);
      uVar2 = FUN_ffff0000b8082b84(3);
      uVar4 = FUN_ffff0000b805135c();
      local_70 = DAT_ffff0000b8133e20;
      uStack_68 = DAT_ffff0000b8133e28;
      local_60 = DAT_ffff0000b8133e30;
      uStack_58 = DAT_ffff0000b8133e38;
      local_80 = DAT_ffff0000b8133e10;
      uStack_78 = DAT_ffff0000b8133e18;
      FUN_ffff0000b8082d3c(uVar2,uVar4,local_28,local_40,&local_80,0xffffffff);
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b8051350(0,0,uVar2,uVar3,0,0,0,0,1);
    }
    if (local_50 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_40);
    }
    if (local_48 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_38);
    }
    if (local_30 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_28);
    }
  }
  return;
}


