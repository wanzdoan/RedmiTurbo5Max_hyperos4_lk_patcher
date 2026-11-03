// ffff000090788100  size=372  FUN_ffff000090788100

undefined4 FUN_ffff000090788100(byte *param_1,uint param_2,long param_3,int param_4,long param_5)

{
  byte bVar1;
  int iVar2;
  ulong uVar3;
  byte *pbVar4;
  undefined4 uVar5;
  byte local_22c [64];
  undefined1 auStack_1ec [284];
  byte local_d0 [64];
  undefined1 auStack_90 [32];
  undefined1 auStack_70 [32];
  
  if (param_1 == (byte *)0x0) {
    uVar5 = 0x102007;
  }
  else if (param_2 < 0x21) {
    if (param_3 == 0) {
      uVar5 = 0x102009;
    }
    else if (param_4 - 0x11dU < 0xfffffee4) {
      uVar5 = 0x10200a;
    }
    else if (param_5 == 0) {
      uVar5 = 0x10200b;
    }
    else {
      FUN_ffff00009077ee50(local_22c,0,0x1dc);
      FUN_ffff00009077ee50(local_22c,0,0x1dc);
      FUN_ffff00009077eb20(auStack_1ec,param_3,param_4);
      FUN_ffff00009077ee50(local_22c,0x36,0x40);
      FUN_ffff00009077ee50(local_d0,0x5c,0x40);
      if (param_2 != 0) {
        uVar3 = (ulong)param_2;
        pbVar4 = local_22c;
        do {
          bVar1 = *param_1;
          uVar3 = uVar3 - 1;
          pbVar4[0x15c] = pbVar4[0x15c] ^ bVar1;
          *pbVar4 = *pbVar4 ^ bVar1;
          pbVar4 = pbVar4 + 1;
          param_1 = param_1 + 1;
        } while (uVar3 != 0);
      }
      iVar2 = FUN_ffff0000907a4318(local_22c,param_4 + 0x40,auStack_90);
      uVar5 = 0x10200c;
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907a4318(local_d0,0x60,auStack_70);
        uVar5 = 0x10200c;
        if (iVar2 == 0) {
          FUN_ffff00009077eb20(param_5,auStack_70,0x20);
          uVar5 = 0;
        }
      }
    }
  }
  else {
    uVar5 = 0x102008;
  }
  return uVar5;
}


