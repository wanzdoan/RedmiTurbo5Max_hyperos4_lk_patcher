
void FUN_ffff0000b8056870(long param_1)

{
  uint uVar1;
  undefined4 uVar2;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (*(long *)(param_1 + 8) != 0) {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    uVar1 = FUN_ffff0000b800e378();
    if ((uVar1 < 5) && (uVar1 != 2)) {
      *(uint *)(*(long *)(param_1 + 8) + 0x90) =
           *(uint *)(*(long *)(param_1 + 8) + 0x90) & 0xfffffffe;
    }
    else {
      FUN_ffff0000b804d070(&local_40,0,0x20);
      FUN_ffff0000b800e6ac(0xc200040d,2,0,0,0,0,0,0,&local_40);
    }
    uVar2 = 1000;
    if (*(short *)(param_1 + 0x88) != 300) {
      uVar2 = 10;
    }
    FUN_ffff0000b804b704(uVar2);
  }
  return;
}


