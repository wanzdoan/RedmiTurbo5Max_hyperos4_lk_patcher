
void FUN_ffff0000b8051d14(int param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined8 uVar4;
  ulong uVar5;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  long local_60;
  long local_58;
  uint *local_50;
  undefined8 local_48;
  long local_40;
  undefined8 local_38;
  
  local_40 = 0;
  local_38 = 0;
  local_50 = (uint *)0x0;
  local_48 = 0;
  local_60 = 0;
  local_58 = 0;
  iVar1 = FUN_ffff0000b805159c(&local_60);
  if (iVar1 == 0) {
    FUN_ffff0000b8051824(&DAT_ffff0000b8133e40);
    DAT_ffff0000b8133dac = 1;
    if (local_50 != (uint *)0x0) {
      DAT_ffff0000b8133dac = (uint)(5 < *local_50);
    }
    uVar2 = FUN_ffff0000b8082b84(param_1 + 0x26);
    uVar4 = FUN_ffff0000b803e898();
    local_80 = DAT_ffff0000b8133e50;
    uStack_78 = DAT_ffff0000b8133e58;
    local_70 = DAT_ffff0000b8133e60;
    uStack_68 = DAT_ffff0000b8133e68;
    local_90 = DAT_ffff0000b8133e40;
    uStack_88 = DAT_ffff0000b8133e48;
    FUN_ffff0000b8082d3c(uVar2,uVar4,local_48,local_50,&local_90,0xffffffff);
    uVar5 = FUN_ffff0000b8051340();
    if (((uVar5 & 1) == 0) || (iVar1 = FUN_ffff0000b80519c0(&local_60), iVar1 != 0)) {
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b803e8ec(0,0,uVar2,uVar3);
    }
    else {
      FUN_ffff0000b8051a90(&DAT_ffff0000b8133e70);
      uVar2 = FUN_ffff0000b8082b84(param_1 + 0x26);
      uVar4 = FUN_ffff0000b805135c();
      local_80 = DAT_ffff0000b8133e80;
      uStack_78 = DAT_ffff0000b8133e88;
      local_70 = DAT_ffff0000b8133e90;
      uStack_68 = DAT_ffff0000b8133e98;
      local_90 = DAT_ffff0000b8133e70;
      uStack_88 = DAT_ffff0000b8133e78;
      FUN_ffff0000b8082d3c(uVar2,uVar4,local_38,local_50,&local_90,0xffffffff);
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b8051350(0,0,uVar2,uVar3,0,0,0,0,1);
    }
    if (local_60 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_50);
    }
    if (local_58 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_48);
    }
    if (local_40 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_38);
    }
  }
  return;
}


