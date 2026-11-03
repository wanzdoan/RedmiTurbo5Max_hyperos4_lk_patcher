// ffff00009076ee9c  size=196  FUN_ffff00009076ee9c

void FUN_ffff00009076ee9c(long param_1,long param_2,int param_3,uint param_4,int param_5)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  
  uVar3 = (*(uint *)(param_1 + 0x10) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x10) & 0xff00ff) << 8;
  lVar1 = (long)param_3 + 0x28 + (long)(int)param_4;
  FUN_ffff00009077ec38(param_2 + 0x28,param_1 + (ulong)(uVar3 >> 0x10 | uVar3 << 0x10));
  uVar2 = (uint)((long)param_3 + 0x28);
  *(undefined4 *)(param_2 + 0x10) = 0x28000000;
  uVar3 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 8) & 0xff00ff) << 8;
  FUN_ffff00009077ec38
            (param_2 + (int)uVar2,param_1 + (ulong)(uVar3 >> 0x10 | uVar3 << 0x10),
             (long)(int)param_4);
  uVar3 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
  uVar2 = (param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8;
  *(uint *)(param_2 + 8) = uVar3 >> 0x10 | uVar3 << 0x10;
  *(uint *)(param_2 + 0x24) = uVar2 >> 0x10 | uVar2 << 0x10;
  uVar3 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
  FUN_ffff00009077ec38
            (param_2 + lVar1,param_1 + (ulong)(uVar3 >> 0x10 | uVar3 << 0x10),(long)param_5);
  uVar3 = (uint)lVar1;
  uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
  *(uint *)(param_2 + 0xc) = uVar3 >> 0x10 | uVar3 << 0x10;
  *(undefined4 *)(param_2 + 0x20) = *(undefined4 *)(param_1 + 0x20);
  return;
}


