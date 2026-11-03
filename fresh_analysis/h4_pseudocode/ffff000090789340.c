
void FUN_ffff000090789340(uint param_1)

{
  byte bVar1;
  undefined *puVar2;
  ulong uVar3;
  ulong uVar4;
  undefined1 uVar5;
  long lVar6;
  ulong uVar7;
  
  puVar2 = PTR_s_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef_ffff0000908366a8;
  if ((int)param_1 < 1) {
    uVar4 = 0;
  }
  else {
    lVar6 = 0;
    uVar3 = 0;
    do {
      uVar4 = ((ulong)*(byte *)((long)&DAT_ffff0000908875a0 + lVar6) & 3) * 0x10;
      (&DAT_ffff000090887398)[uVar3] = puVar2[*(byte *)((long)&DAT_ffff0000908875a0 + lVar6) >> 2];
      if ((ulong)param_1 <= lVar6 + 1U) {
        uVar7 = uVar3 & 0xfffffffc | 2;
        uVar5 = 0x3d;
        (&DAT_ffff000090887398)[(int)uVar3 + 1] = puVar2[uVar4];
LAB_ffff000090789428:
        uVar4 = uVar3 + 4;
        (&DAT_ffff000090887398)[uVar7] = uVar5;
        (&DAT_ffff00009088739b)[uVar3 & 0xfffffffc] = 0x3d;
        break;
      }
      bVar1 = *(byte *)((long)&DAT_ffff0000908875a0 + lVar6 + 1);
      uVar7 = ((ulong)bVar1 & 0xf) * 4;
      (&DAT_ffff000090887399)[uVar3] = puVar2[uVar4 | bVar1 >> 4];
      if ((int)param_1 <= (int)lVar6 + 2) {
        uVar5 = puVar2[uVar7];
        uVar7 = (ulong)((int)uVar3 + 2);
        goto LAB_ffff000090789428;
      }
      bVar1 = *(byte *)((long)&DAT_ffff0000908875a0 + lVar6 + 2);
      uVar4 = uVar3 + 4;
      lVar6 = lVar6 + 3;
      (&DAT_ffff00009088739a)[uVar3] = puVar2[uVar7 | bVar1 >> 6];
      (&DAT_ffff00009088739b)[uVar3] = puVar2[(ulong)bVar1 & 0x3f];
      uVar3 = uVar4;
    } while ((int)lVar6 < (int)param_1);
  }
  (&DAT_ffff000090887398)[(int)uVar4] = 0;
  return;
}


