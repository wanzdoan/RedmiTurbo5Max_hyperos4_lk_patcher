// File offset entry: 0xa3f54

undefined8 FUN_020a3e54(uint param_1,uint param_2,uint param_3)

{
  uint uVar1;
  long lVar2;
  uint uVar3;
  undefined8 uVar4;
  ulong uVar5;
  int *piVar6;
  
  uVar5 = (ulong)param_1;
  if (param_1 < 8) {
    piVar6 = (int *)(uVar5 * 0x14 + 0x2100e30);
    if (*piVar6 == -1) {
      *piVar6 = 0x18790314;
    }
    uVar3 = param_2 | param_3 ^ 0xffffffff;
    uVar4 = 0;
    lVar2 = uVar5 * 0x14;
    uVar1 = *(uint *)(ulong)*(uint *)(lVar2 + 0x2100e34);
    *(uint *)(ulong)*(uint *)(lVar2 + 0x2100e34) =
         param_2 & (uVar1 ^ 0xffffffff) & param_3 | uVar1 & uVar3;
    *(uint *)(lVar2 + 0x2100e3c) =
         param_2 & (*(uint *)(lVar2 + 0x2100e3c) ^ 0xffffffff) & param_3 |
         *(uint *)(lVar2 + 0x2100e3c) & uVar3;
    *(uint *)(lVar2 + 0x2100e40) = *(uint *)(lVar2 + 0x2100e40) | param_3;
  }
  else {
    FUN_0208ada0(0x20c98f4,0x20cbd2f,uVar5,8);
    uVar4 = 0x107000;
  }
  return uVar4;
}


