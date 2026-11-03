// ffff000090724df0  size=356  FUN_ffff000090724df0

void FUN_ffff000090724df0(long param_1)

{
  uint *puVar1;
  long lVar2;
  bool bVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  
  lVar4 = param_1 + 0xffffc;
  lVar6 = 0xff;
  do {
    lVar5 = 0;
    lVar7 = 0x3df00000000;
    do {
      puVar1 = (uint *)(lVar4 + lVar5);
      lVar5 = lVar5 + -0x80;
      puVar1[-1] = ~puVar1[-1];
      *puVar1 = ~*puVar1;
      puVar1[-3] = ~puVar1[-3];
      puVar1[-2] = ~puVar1[-2];
      puVar1[-5] = ~puVar1[-5];
      puVar1[-4] = ~puVar1[-4];
      puVar1[-7] = ~puVar1[-7];
      puVar1[-6] = ~puVar1[-6];
      puVar1[-9] = ~puVar1[-9];
      puVar1[-8] = ~puVar1[-8];
      puVar1[-0xd] = ~puVar1[-0xd];
      puVar1[-0xc] = ~puVar1[-0xc];
      puVar1[-0xb] = ~puVar1[-0xb];
      puVar1[-10] = ~puVar1[-10];
      puVar1[-0xf] = ~puVar1[-0xf];
      puVar1[-0xe] = ~puVar1[-0xe];
      puVar1[-0x11] = ~puVar1[-0x11];
      puVar1[-0x10] = ~puVar1[-0x10];
      puVar1[-0x13] = ~puVar1[-0x13];
      puVar1[-0x12] = ~puVar1[-0x12];
      puVar1[-0x15] = ~puVar1[-0x15];
      puVar1[-0x14] = ~puVar1[-0x14];
      puVar1[-0x17] = ~puVar1[-0x17];
      puVar1[-0x16] = ~puVar1[-0x16];
      puVar1[-0x19] = ~puVar1[-0x19];
      puVar1[-0x18] = ~puVar1[-0x18];
      puVar1[-0x1b] = ~puVar1[-0x1b];
      puVar1[-0x1a] = ~puVar1[-0x1a];
      puVar1[-0x1d] = ~puVar1[-0x1d];
      puVar1[-0x1c] = ~puVar1[-0x1c];
      lVar2 = param_1 + lVar6 * 0x1000 + (lVar7 >> 0x1e);
      lVar7 = lVar7 + -0x2000000000;
      puVar1[-0x1f] = ~puVar1[-0x1f];
      puVar1[-0x1e] = ~puVar1[-0x1e];
      DC_CVAU(lVar2);
      DataSynchronizationBarrier(2,3,0);
      IC_IVAU(lVar2);
      DataSynchronizationBarrier(2,3,0);
      InstructionSynchronizationBarrier();
    } while ((int)lVar5 != -0x1000);
    lVar4 = lVar4 + -0x1000;
    bVar3 = lVar6 != 0;
    lVar6 = lVar6 + -1;
  } while (bVar3);
  return;
}


