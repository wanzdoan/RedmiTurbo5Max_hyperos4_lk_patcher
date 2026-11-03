// ffff000090717cdc  size=192  FUN_ffff000090717cdc

undefined4 FUN_ffff000090717cdc(long param_1,int param_2)

{
  long lVar1;
  undefined4 uVar2;
  long *plVar3;
  long *plVar4;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  lVar1 = tpidr_el1;
  if (param_2 == 0) {
    uVar2 = 0xfffffff3;
  }
  else {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    plVar3 = (long *)(param_1 + 8);
    local_50 = 0;
    uStack_48 = 0;
    plVar4 = (long *)(lVar1 + 0x18);
    *plVar4 = *plVar3;
    *(long **)(lVar1 + 0x20) = plVar3;
    *(long **)(*plVar3 + 8) = plVar4;
    *plVar3 = (long)plVar4;
    *(int *)(param_1 + 0x18) = *(int *)(param_1 + 0x18) + 1;
    *(undefined4 *)(lVar1 + 0x2c) = 3;
    *(long *)(lVar1 + 0x40) = param_1;
    *(undefined4 *)(lVar1 + 0x48) = 0;
    if (param_2 == -1) {
      FUN_ffff000090717fe8();
    }
    else {
      FUN_ffff000090718584(&local_50);
      FUN_ffff0000907185b4(&local_50,param_2,FUN_ffff00009071839c,lVar1);
      FUN_ffff000090717fe8();
      FUN_ffff00009071874c(&local_50);
    }
    uVar2 = *(undefined4 *)(lVar1 + 0x48);
  }
  return uVar2;
}


