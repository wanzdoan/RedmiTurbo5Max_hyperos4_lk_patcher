
void FUN_ffff0000b80520cc(undefined4 param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  ulong uVar4;
  long lVar5;
  undefined8 uVar6;
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
  uVar4 = thunk_FUN_ffff0000b803f20c();
  lVar5 = FUN_ffff0000b803e984();
  iVar1 = FUN_ffff0000b805159c(&local_60);
  if (iVar1 == 0) {
    FUN_ffff0000b8051824(&DAT_ffff0000b8133f00);
    DAT_ffff0000b8133dac = 1;
    if (local_50 != (uint *)0x0) {
      DAT_ffff0000b8133dac = (uint)(5 < *local_50);
    }
    uVar6 = FUN_ffff0000b803e898();
    local_80 = DAT_ffff0000b8133f10;
    uStack_78 = DAT_ffff0000b8133f18;
    local_70 = DAT_ffff0000b8133f20;
    uStack_68 = DAT_ffff0000b8133f28;
    local_90 = DAT_ffff0000b8133f00;
    uStack_88 = DAT_ffff0000b8133f08;
    FUN_ffff0000b8083de8(uVar6,0,uVar4 & 0xffffffff | lVar5 << 0x20,param_1,&local_90);
    uVar4 = FUN_ffff0000b8051340();
    if (((uVar4 & 1) == 0) || (iVar1 = FUN_ffff0000b80519c0(&local_60), iVar1 != 0)) {
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b803e8ec(0,0,uVar2,uVar3);
    }
    else {
      FUN_ffff0000b8051a90(&DAT_ffff0000b8133f30);
      uVar6 = FUN_ffff0000b805135c();
      local_80 = DAT_ffff0000b8133f40;
      uStack_78 = DAT_ffff0000b8133f48;
      local_70 = DAT_ffff0000b8133f50;
      uStack_68 = DAT_ffff0000b8133f58;
      local_90 = DAT_ffff0000b8133f30;
      uStack_88 = DAT_ffff0000b8133f38;
      FUN_ffff0000b8083de8(uVar6,0,0,param_1,&local_90);
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


