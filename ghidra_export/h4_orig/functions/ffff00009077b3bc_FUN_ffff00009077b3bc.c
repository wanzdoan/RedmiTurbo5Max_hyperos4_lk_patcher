// ffff00009077b3bc  size=376  FUN_ffff00009077b3bc

void FUN_ffff00009077b3bc(void)

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
  
  FUN_ffff00009077b5b8();
  puVar2 = PTR_LAB_ffff000090839870;
  local_50 = &local_50;
  lVar4 = DAT_ffff000090839880 - (long)PTR_LAB_ffff000090839870;
  if ((lVar4 + ((ulong)PTR_LAB_ffff000090839870 & 0xfff) + 0xfff & 0xfffffffffffff000) != 0) {
    uVar5 = 0;
    uVar7 = (ulong)PTR_LAB_ffff000090839870 & 0xfffffffffffff000;
    lVar1 = DAT_ffff000090839928 + 0x58;
    ppuStack_48 = local_50;
    do {
      local_24 = 0;
      local_58 = 0;
      iVar3 = FUN_ffff000090708784(lVar1,uVar5 + uVar7,&local_58,&local_24);
      if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cee0
                  (s_Could_not_find_pa_for_va_0x_lx_ffff0000907fe1d4,
                   (ulong)puVar2 & 0xfffffffffffff000);
      }
      FUN_ffff00009077ad8c(local_58,1,&local_50);
      uVar5 = uVar5 + 0x1000;
    } while (uVar5 < (lVar4 + ((ulong)puVar2 & 0xfff) + 0xfff & 0xfffffffffffff000));
  }
  uVar5 = *(ulong *)PTR_DAT_ffff000090839a20;
  if (*(long *)PTR_DAT_ffff000090839878 - uVar5 != 0) {
    local_50 = &local_50;
    uVar7 = (*(long *)PTR_DAT_ffff000090839878 - uVar5) + (uVar5 & 0xfff) + 0xfff &
            0xfffffffffffff000;
    if (uVar7 != 0) {
      uVar6 = 0;
      lVar4 = DAT_ffff000090839928 + 0x58;
      ppuStack_48 = local_50;
      do {
        local_24 = 0;
        local_58 = 0;
        iVar3 = FUN_ffff000090708784(lVar4,(uVar5 & 0xfffffffffffff000) + uVar6,&local_58,&local_24)
        ;
        if (iVar3 < 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cee0
                    (s_Could_not_find_pa_for_va_0x_lx_ffff0000907fe1d4,uVar5 & 0xfffffffffffff000);
        }
        FUN_ffff00009077ad8c(local_58,1,&local_50);
        uVar6 = uVar6 + 0x1000;
      } while (uVar6 < uVar7);
    }
  }
  return;
}


