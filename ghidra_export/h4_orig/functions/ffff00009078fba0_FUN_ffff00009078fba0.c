// ffff00009078fba0  size=220  FUN_ffff00009078fba0

undefined4 FUN_ffff00009078fba0(undefined8 param_1,undefined8 param_2,undefined4 param_3)

{
  uint uVar1;
  undefined4 uVar2;
  ulong uVar3;
  uint local_64;
  long local_60;
  undefined4 local_58;
  undefined4 local_54;
  undefined1 uStack_50;
  undefined2 uStack_4f;
  undefined1 uStack_4d;
  undefined1 uStack_4c;
  undefined1 uStack_4b;
  undefined1 uStack_4a;
  undefined1 uStack_49;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 local_38;
  
  local_40 = 0;
  local_38 = 0;
  uStack_50 = 0;
  uStack_4f = 0;
  uStack_4d = 0;
  uStack_4c = 0;
  uStack_4b = 0;
  uStack_4a = 0;
  uStack_49 = 0;
  uStack_48 = 0;
  local_60 = 0;
  local_58 = 0;
  local_54 = 0;
  local_64 = 0;
  uVar3 = FUN_ffff00009078ff68(param_1,&local_64);
  uVar1 = local_64;
  if ((uVar3 & 1) == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    FUN_ffff00009077ee50((long)&local_54 + 1,0,0x10);
    local_60 = (ulong)uVar1 << 0x20;
    local_58 = 1;
    uStack_4d = (undefined1)((uint)param_3 >> 0x10);
    uStack_48 = CONCAT26(10,(undefined6)uStack_48);
    uStack_50 = 0;
    local_40 = CONCAT44(local_40._4_4_,param_3);
    local_54 = 0xe3b00;
    uStack_4f = 0;
    uStack_4c = (undefined1)((uint)param_3 >> 8);
    uStack_4b = (undefined1)param_3;
    uStack_4a = 0;
    local_38 = param_2;
    uVar2 = FUN_ffff00009079101c(param_1,&local_60);
    FUN_ffff000090790038(param_1,local_64);
  }
  return uVar2;
}


