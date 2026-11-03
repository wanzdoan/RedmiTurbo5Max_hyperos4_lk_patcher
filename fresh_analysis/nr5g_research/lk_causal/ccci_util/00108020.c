
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void free_reserved_memory_ccci(ulong param_1,ulong param_2)

{
  ulong *puVar1;
  char cVar2;
  bool bVar3;
  undefined8 uVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  ulong uVar8;
  ulong local_78;
  ulong uStack_70;
  long local_68;
  
  lVar6 = sp_el0;
  local_68 = *(long *)(lVar6 + 0x620);
  local_78 = param_2;
  uStack_70 = param_1;
  if (param_1 < param_2) {
    lVar7 = 0;
    lVar6 = 0;
    uVar8 = param_1;
    do {
      lVar5 = (_memstart_addr >> 0xc) * -0x40 + -0x200000000;
      puVar1 = (ulong *)(lVar5 + (uVar8 >> 0xc) * 0x40);
      Hint_Prefetch(puVar1,2,0,1);
      do {
        cVar2 = '\x01';
        bVar3 = (bool)ExclusiveMonitorPass(puVar1,0x10);
        if (bVar3) {
          *puVar1 = *puVar1 & 0xffffffffffffbfff;
          cVar2 = ExclusiveMonitorsStatus();
        }
      } while (cVar2 != '\0');
      *(undefined4 *)(lVar5 + (uVar8 >> 0xc) * 0x40 + 0x34) = 1;
      __free_pages(puVar1,0);
      adjust_managed_page_count(puVar1,1);
      uVar8 = uVar8 + 0x1000;
      lVar6 = lVar6 + 4;
      lVar7 = lVar7 + -1;
    } while (uVar8 < param_2);
    if (lVar7 != 0) {
      _printk(&DAT_001123a0,lVar6,param_1);
    }
    uVar4 = 0;
  }
  else {
    _printk(&DAT_00111c8f,"free_reserved_memory_ccci",&uStack_70,&local_78);
    uVar4 = 0xffffffff;
  }
  lVar6 = sp_el0;
  if (*(long *)(lVar6 + 0x620) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar4);
}

