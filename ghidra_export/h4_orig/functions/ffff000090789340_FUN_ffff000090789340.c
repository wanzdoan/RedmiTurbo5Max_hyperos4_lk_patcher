// ffff000090789340  size=272  FUN_ffff000090789340

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
      uVar4 = ((ulong)(byte)(&DAT_ffff0000908875a0)[lVar6] & 3) * 0x10;
      (&DAT_ffff000090887398)[uVar3] = puVar2[(byte)(&DAT_ffff0000908875a0)[lVar6] >> 2];
      if ((ulong)param_1 <= lVar6 + 1U) {
        uVar7 = uVar3 & 0xfffffffc | 2;
        uVar5 = 0x3d;
        (&DAT_ffff000090887398)[(int)uVar3 + 1] = puVar2[uVar4];
LAB_ffff000090789428:
        uVar4 = uVar3 + 4;
        (&DAT_ffff000090887398)[uVar7] = uVar5;
        *(undefined1 *)((uVar3 & 0xfffffffc) - 0xffff6f778c65) = 0x3d;
        break;
      }
      uVar7 = ((ulong)*(byte *)(lVar6 + -0xffff6f778a5f) & 0xf) * 4;
      *(undefined *)(uVar3 - 0xffff6f778c67) =
           puVar2[uVar4 | *(byte *)(lVar6 + -0xffff6f778a5f) >> 4];
      if ((int)param_1 <= (int)lVar6 + 2) {
        uVar5 = puVar2[uVar7];
        uVar7 = (ulong)((int)uVar3 + 2);
        goto LAB_ffff000090789428;
      }
      bVar1 = *(byte *)(lVar6 + -0xffff6f778a5e);
      uVar4 = uVar3 + 4;
      lVar6 = lVar6 + 3;
      *(undefined *)(uVar3 - 0xffff6f778c66) = puVar2[uVar7 | bVar1 >> 6];
      *(undefined *)(uVar3 - 0xffff6f778c65) = puVar2[(ulong)bVar1 & 0x3f];
      uVar3 = uVar4;
    } while ((int)lVar6 < (int)param_1);
  }
  (&DAT_ffff000090887398)[(int)uVar4] = 0;
  return;
}


