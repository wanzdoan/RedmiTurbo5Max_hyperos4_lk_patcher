// ffff00009077b0f4  size=376  FUN_ffff00009077b0f4

void FUN_ffff00009077b0f4(void)

{
  long lVar1;
  undefined *puVar2;
  int iVar3;
  long lVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  undefined8 local_58;
  undefined8 **local_50;
  undefined8 **ppuStack_48;
  undefined4 local_24;
  
  FUN_ffff00009077b2f0();
  puVar2 = PTR_LAB_ffff000090839850;
  local_50 = &local_50;
  lVar4 = DAT_ffff000090839860 - (long)PTR_LAB_ffff000090839850;
  if ((lVar4 + ((ulong)PTR_LAB_ffff000090839850 & 0xfff) + 0xfff & 0xfffffffffffff000) != 0) {
    uVar5 = 0;
    uVar7 = (ulong)PTR_LAB_ffff000090839850 & 0xfffffffffffff000;
    lVar1 = DAT_ffff000090839908 + 0x58;
    ppuStack_48 = local_50;
    do {
      local_24 = 0;
      local_58 = 0;
      iVar3 = FUN_ffff0000907087f8(lVar1,uVar5 + uVar7,&local_58,&local_24);
      if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cc18
                  (s_Could_not_find_pa_for_va_0x_lx_ffff0000907fe02c,
                   (ulong)puVar2 & 0xfffffffffffff000);
      }
      FUN_ffff00009077aac4(local_58,1,&local_50);
      uVar5 = uVar5 + 0x1000;
    } while (uVar5 < (lVar4 + ((ulong)puVar2 & 0xfff) + 0xfff & 0xfffffffffffff000));
  }
  uVar5 = *(ulong *)PTR_DAT_ffff000090839a00;
  if (*(long *)PTR_DAT_ffff000090839858 - uVar5 != 0) {
    local_50 = &local_50;
    uVar7 = (*(long *)PTR_DAT_ffff000090839858 - uVar5) + (uVar5 & 0xfff) + 0xfff &
            0xfffffffffffff000;
    if (uVar7 != 0) {
      uVar6 = 0;
      lVar4 = DAT_ffff000090839908 + 0x58;
      ppuStack_48 = local_50;
      do {
        local_24 = 0;
        local_58 = 0;
        iVar3 = FUN_ffff0000907087f8(lVar4,(uVar5 & 0xfffffffffffff000) + uVar6,&local_58,&local_24)
        ;
        if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cc18
                    (s_Could_not_find_pa_for_va_0x_lx_ffff0000907fe02c,uVar5 & 0xfffffffffffff000);
        }
        FUN_ffff00009077aac4(local_58,1,&local_50);
        uVar6 = uVar6 + 0x1000;
      } while (uVar6 < uVar7);
    }
  }
  return;
}


