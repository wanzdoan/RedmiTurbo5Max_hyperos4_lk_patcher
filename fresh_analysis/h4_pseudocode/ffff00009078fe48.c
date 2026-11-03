
undefined8 FUN_ffff00009078fe48(long *param_1,long param_2,uint param_3)

{
  undefined4 uVar1;
  undefined8 uVar2;
  long lVar3;
  byte *pbVar4;
  long lVar5;
  ulong uVar6;
  ulong uVar7;
  
  if ((int)param_3 < 1) {
    uVar2 = 0;
  }
  else {
    uVar6 = 0;
    lVar3 = *param_1;
    do {
      if ((*(uint *)(lVar3 + 0x30) >> 3 & 1) == 0) {
        uVar2 = 0xffffffff;
      }
      else {
        pbVar4 = (byte *)(param_2 + uVar6 * 0x18);
        uVar7 = 0xffffffffffffff9c;
        uVar1 = *(undefined4 *)(pbVar4 + 4);
        param_1[10] = (long)pbVar4;
        *(undefined4 *)(lVar3 + 0x94) = uVar1;
        *(undefined4 *)(lVar3 + 0x98) = *(undefined4 *)(pbVar4 + 8);
        *(undefined4 *)(lVar3 + 0x9c) = *(undefined4 *)(pbVar4 + 0xc);
        *(uint *)(lVar3 + 0x90) = (uint)*pbVar4;
        do {
          lVar3 = *param_1;
          if ((*(uint *)(lVar3 + 0x20) >> 10 & 1) != 0) goto LAB_ffff00009078ff0c;
          FUN_ffff00009077cea0(100);
          uVar7 = uVar7 + 100;
        } while (uVar7 < 0x7a0bd);
        lVar3 = *param_1;
        if ((*(uint *)(lVar3 + 0x20) >> 10 & 1) == 0) {
          uVar2 = 0xffffffff;
        }
        else {
LAB_ffff00009078ff0c:
          *(undefined4 *)(lVar3 + 0x20) = 0x400;
          lVar5 = param_1[10];
          if (lVar5 == 0) {
            uVar2 = 0;
          }
          else {
            uVar2 = 0;
            *(uint *)(lVar5 + 8) = *(uint *)(lVar5 + 8) | *(uint *)(lVar3 + 0x98) & 0xff;
            *(undefined4 *)(lVar5 + 0xc) = *(undefined4 *)(lVar3 + 0x9c);
          }
        }
        param_1[10] = 0;
      }
      uVar6 = uVar6 + 1;
    } while (uVar6 != param_3);
  }
  return uVar2;
}


