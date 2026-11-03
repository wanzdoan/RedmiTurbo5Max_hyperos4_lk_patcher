// ffff00009078f6d8  size=260  FUN_ffff00009078f6d8

undefined4
FUN_ffff00009078f6d8
          (undefined8 param_1,undefined4 param_2,ulong param_3,int param_4,undefined8 param_5)

{
  undefined8 uVar1;
  undefined4 uVar2;
  ulong uVar3;
  ulong uVar4;
  undefined4 local_74;
  undefined8 local_70;
  undefined8 local_68;
  undefined1 local_60 [2];
  undefined1 uStack_5e;
  undefined1 uStack_5c;
  undefined1 uStack_5b;
  undefined1 uStack_59;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 local_48;
  
  uVar4 = param_3 & 0xffffffff;
  local_50 = 0;
  local_48 = 0;
  _local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  local_68 = 0;
  local_74 = 0;
  uVar3 = FUN_ffff00009078fca8(param_1,&local_74);
  uVar2 = local_74;
  if ((uVar3 & 1) == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    FUN_ffff00009077eb88((long)&local_68 + 5,0,0x10);
    uVar1 = _local_60;
    local_70 = CONCAT44(uVar2,param_2);
    local_50 = CONCAT44(local_50._4_4_,param_4 << 0xc);
    uStack_58 = CONCAT26(10,(undefined6)uStack_58);
    local_68 = CONCAT17((char)(uVar4 >> 0x18),0x8280000000002);
    local_68 = CONCAT35(local_68._5_3_,2);
    _local_60 = (uint)CONCAT12((char)param_3,CONCAT11((char)(uVar4 >> 8),(char)(uVar4 >> 0x10)));
    uStack_59 = SUB81(uVar1,7);
    _local_60 = (uint7)CONCAT15((char)param_4,CONCAT14((char)((uint)param_4 >> 8),_local_60));
    local_48 = param_5;
    uVar2 = FUN_ffff000090790d5c(param_1,&local_70);
    FUN_ffff00009078fd78(param_1,local_74);
  }
  return uVar2;
}


