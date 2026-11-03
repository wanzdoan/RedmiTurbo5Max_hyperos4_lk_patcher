// ffff000090724a5c  size=336  FUN_ffff000090724a5c

void FUN_ffff000090724a5c(long param_1)

{
  long lVar1;
  uint uVar2;
  long lVar3;
  
  lVar1 = 0;
  param_1 = param_1 + 0x80;
  do {
    uVar2 = 0xffffffe0;
    lVar3 = param_1;
    do {
      uVar2 = uVar2 + 0x20;
      *(uint *)(lVar3 + -0x80) = ~*(uint *)(lVar3 + -0x80);
      *(uint *)(lVar3 + -0x7c) = ~*(uint *)(lVar3 + -0x7c);
      *(uint *)(lVar3 + -0x78) = ~*(uint *)(lVar3 + -0x78);
      *(uint *)(lVar3 + -0x74) = ~*(uint *)(lVar3 + -0x74);
      *(uint *)(lVar3 + -0x70) = ~*(uint *)(lVar3 + -0x70);
      *(uint *)(lVar3 + -0x6c) = ~*(uint *)(lVar3 + -0x6c);
      *(uint *)(lVar3 + -0x68) = ~*(uint *)(lVar3 + -0x68);
      *(uint *)(lVar3 + -100) = ~*(uint *)(lVar3 + -100);
      *(uint *)(lVar3 + -0x60) = ~*(uint *)(lVar3 + -0x60);
      *(uint *)(lVar3 + -0x5c) = ~*(uint *)(lVar3 + -0x5c);
      *(uint *)(lVar3 + -0x58) = ~*(uint *)(lVar3 + -0x58);
      *(uint *)(lVar3 + -0x54) = ~*(uint *)(lVar3 + -0x54);
      *(uint *)(lVar3 + -0x50) = ~*(uint *)(lVar3 + -0x50);
      *(uint *)(lVar3 + -0x4c) = ~*(uint *)(lVar3 + -0x4c);
      *(uint *)(lVar3 + -0x48) = ~*(uint *)(lVar3 + -0x48);
      *(uint *)(lVar3 + -0x44) = ~*(uint *)(lVar3 + -0x44);
      *(uint *)(lVar3 + -0x40) = ~*(uint *)(lVar3 + -0x40);
      *(uint *)(lVar3 + -0x3c) = ~*(uint *)(lVar3 + -0x3c);
      *(uint *)(lVar3 + -0x38) = ~*(uint *)(lVar3 + -0x38);
      *(uint *)(lVar3 + -0x34) = ~*(uint *)(lVar3 + -0x34);
      *(uint *)(lVar3 + -0x30) = ~*(uint *)(lVar3 + -0x30);
      *(uint *)(lVar3 + -0x2c) = ~*(uint *)(lVar3 + -0x2c);
      *(uint *)(lVar3 + -0x28) = ~*(uint *)(lVar3 + -0x28);
      *(uint *)(lVar3 + -0x24) = ~*(uint *)(lVar3 + -0x24);
      *(uint *)(lVar3 + -0x20) = ~*(uint *)(lVar3 + -0x20);
      *(uint *)(lVar3 + -0x1c) = ~*(uint *)(lVar3 + -0x1c);
      *(uint *)(lVar3 + -0x18) = ~*(uint *)(lVar3 + -0x18);
      *(uint *)(lVar3 + -0x14) = ~*(uint *)(lVar3 + -0x14);
      *(uint *)(lVar3 + -0x10) = ~*(uint *)(lVar3 + -0x10);
      *(uint *)(lVar3 + -0xc) = ~*(uint *)(lVar3 + -0xc);
      *(uint *)(lVar3 + -8) = ~*(uint *)(lVar3 + -8);
      *(uint *)(lVar3 + -4) = ~*(uint *)(lVar3 + -4);
      DC_CVAU(lVar3);
      DataSynchronizationBarrier(2,3,0);
      IC_IVAU(lVar3);
      lVar3 = lVar3 + 0x80;
      DataSynchronizationBarrier(2,3,0);
      InstructionSynchronizationBarrier();
    } while (uVar2 < 0x3e0);
    lVar1 = lVar1 + 1;
    param_1 = param_1 + 0x1000;
  } while (lVar1 != 0x100);
  return;
}


